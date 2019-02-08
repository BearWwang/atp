import random
import pickle
import json
import numpy as np
import tensorflow as tf
from tqdm import tqdm

from sequence_to_sequence import SequenceToSequence
from data_utils import batch_flow_bucket as batch_flow
from threadedgenerator import ThreadedGenerator


def train(params):
    x_data, y_data = pickle.load(open('chatbot.pkl', 'rb'))
    ws = pickle.load(open('ws.pkl', 'rb'))

    # 训练
    """
    1.n_epoch是训练的轮次数；
    2.理论上来讲训练的轮次数越大，那么训练的精度越高；
    3.如果训练的轮次数特别大，比如1000，那么有可能会发生过拟合的现象，但是是否过拟合也和训练的数据有关
    4.n_epoch越大，训练的时间越长；
    """
    n_epoch = 2
    batch_size = 128

    steps = int(len(x_data) / batch_size) + 1

    config = tf.ConfigProto(
        allow_soft_placement=True,
        log_device_placement=False
    )

    save_path = './model/s2ss_chatbot.ckpt'

    tf.reset_default_graph()
    with tf.Graph().as_default():
        random.seed(0)
        np.random.seed(0)
        tf.set_random_seed(0)

        with tf.Session(config=config) as sess:
            # 定义模型
            model = SequenceToSequence(
                input_vocab_size=len(ws),
                target_vocab_size=len(ws),
                batch_size=batch_size,
                **params
            )
            init = tf.global_variables_initializer()
            sess.run(init)

            flow = ThreadedGenerator(
                batch_flow([x_data, y_data], ws, batch_size, add_end=[False, True]),
                queue_maxsize=30
            )

            for epoch in range(1, n_epoch + 1):
                costs = []
                bar = tqdm(range(steps), total=steps,
                           desc='epoch {}, loss=0.000000'.format(epoch))
                for _ in bar:
                    x, xl, y, yl = next(flow)
                    x = np.flip(x, axis=1)
                    cost, lr = model.train(sess, x, xl, y, yl, return_lr=True)
                    costs.append(cost)
                    bar.set_description('epoch {} loss={:.6f} lr={:.6f}'.format(
                        epoch,
                        np.mean(costs),
                        lr
                    ))

                model.save(sess, save_path)
    tf.reset_default_graph()
    model_pred = SequenceToSequence(
        input_vocab_size=len(ws),
        target_vocab_size=len(ws),
        batch_size=1,
        mode='decode',
        beam_width=12,
        parallel_iterations=1,
        **params
    )

    init = tf.global_variables_initializer()

    with tf.Session(config=config) as sess:
        sess.run(init)
        model_pred.load(sess, save_path)

        bar = batch_flow([x_data, y_data], ws, 1, add_end=False)
        t = 0
        for x, xl, y, yl in bar:
            x = np.flip(x, axis=1)
            pred = model_pred.predict(
                sess,
                np.array(x),
                np.array(xl)
            )
            print(ws.inverse_transform(x[0]))
            print(ws.inverse_transform(y[0]))
            print(ws.inverse_transform(pred[0]))
            t += 1
            if t >= 3:
                break


if __name__ == '__main__':
    train(json.load(open('params.json')))
