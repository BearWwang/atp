import json
import pickle

import numpy as np
import tensorflow as tf
from flask import Flask, request
from flask.json import jsonify
from sequence_to_sequence import SequenceToSequence
from data_utils import batch_flow

app = Flask(__name__)


def run(info):
    x_data, _ = pickle.load(open('train.pkl', 'rb'))
    ws = pickle.load(open('train_input.pkl', 'rb'))
    config = tf.ConfigProto(
        device_count={'CPU': 1, 'GPU': 0},
        allow_soft_placement=True,
        log_device_placement=False
    )
    save_path = './train_finish.ckpt'
    tf.reset_default_graph()
    params = json.load(open('params.json'))
    model_pred = SequenceToSequence(
        input_vocab_size=len(ws),
        target_vocab_size=len(ws),
        batch_size=1,
        mode='decode',
        beam_width=0,
        **params
    )
    init = tf.global_variables_initializer()
    with tf.Session(config=config) as sess:
        sess.run(init)
        model_pred.load(sess, save_path)
        while True:
            x_test = [list(info.lower())]
            bar = batch_flow([x_test], ws, 1)
            x, xl = next(bar)
            x = np.flip(x, axis=1)
            pred = model_pred.predict(
                sess,
                np.array(x),
                np.array(xl)
            )
            for p in pred:
                ans = ws.inverse_transform(p)
                return ans


@app.route('/api/bot', methods=['get'])
def chatbot():
    infos = request.args['infos']
    print(infos)
    text = run(infos)
    res = jsonify({"text": "".join(text).replace("</s>", "")})
    res.headers['Access-Control-Allow-Origin'] = '*'
    return res


if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0', port=8000)
