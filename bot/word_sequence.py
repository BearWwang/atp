import numpy as np


class WordSequence:
    PAD_TAG = '<pad>'
    UNK_TAG = '<unk>'
    START_TAG = '<s>'
    END_TAG = '</s>'

    PAD = 0
    UNK = 1
    START = 2
    END = 3

    def __init__(self):
        # 初始化基本的字典dict
        self.dicts = {
            WordSequence.PAD_TAG: WordSequence.PAD,
            WordSequence.UNK_TAG: WordSequence.UNK,
            WordSequence.START_TAG: WordSequence.START,
            WordSequence.END_TAG: WordSequence.END,
        }
        self.fited = False

    def to_index(self, word):
        assert self.fited, "WordSequence must fit "
        if word in self.dicts:
            return self.dicts[word]
        return WordSequence.UNK

    def to_word(self, index):
        assert self.fited, "WordSequence must fit "
        for k, v in self.dicts.items():
            if v == index:
                return k
        return WordSequence.UNK_TAG

    def size(self):

        assert self.fited, "WordSequence must fit "
        return len(self.dicts) + 1

    def __len__(self):
        return self.size()

    def fit(self, sentences, min_count=5, max_count=None, max_features=None):

        assert not self.fited, 'WordSequence fit once'
        count = {}
        for sentence in sentences:
            arr = list(sentence)
            for a in arr:
                if a not in count:
                    count[a] = 0
                count[a] += 1

        if min_count is not None:
            count = {k: v for k, v in count.items() if v >= min_count}

        if max_count is not None:
            count = {k: v for k, v in count.items() if v <= max_count}

        if isinstance(max_features, int):
            count = sorted(list(count.items()), key=lambda x: x[1])
            if max_features is not None and len(count) > max_features:
                count = count[-int(max_features):]
            for w, _ in count:
                self.dicts[w] = len(self.dicts)
        else:
            for w in sorted(count.keys()):
                self.dicts[w] = len(self.dicts)

        self.fited = True

    def transform(self, sentence, max_len=None):
        assert self.fited, "WordSequence must fit"

        if max_len is not None:
            r = [self.PAD] * max_len
        else:
            r = [self.PAD] * len(sentence)

        for index, a in enumerate(sentence):
            if max_len is not None and index >= len(r):
                break
            r[index] = self.to_index(a)

        return np.array(r)

    def inverse_transform(self, indices,
                          ignore_pad=False, ignore_unk=False,
                          ignore_start=False, igonre_end=False):
        ret = []
        for i in indices:
            word = self.to_word(i)
            if word == WordSequence.PAD_TAG and ignore_pad:
                continue
            if word == WordSequence.UNK_TAG and ignore_unk:
                continue
            if word == WordSequence.START_TAG and ignore_start:
                continue
            if word == WordSequence.END_TAG and igonre_end:
                continue
            ret.append(word)

        return ret

