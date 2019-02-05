package com.snake.genrater;

import com.snake.genrater.service.FakerIDN;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.join;

public class Company {

    private final Genrater genrater;

    protected Company(Genrater genrater) {
        this.genrater = genrater;
    }

    public String name() {
        return genrater.fakeValuesService().resolve("company.name", this, genrater);
    }

    public String suffix() {
        return genrater.fakeValuesService().resolve("company.suffix", this, genrater);
    }

    public String industry() {
        return genrater.fakeValuesService().resolve("company.industry", this, genrater);
    }

    public String profession() {
        return genrater.fakeValuesService().resolve("company.profession", this, genrater);
    }

    public String buzzword() {
        @SuppressWarnings("unchecked")
        List<List<String>> buzzwordLists = (List<List<String>>) genrater.fakeValuesService().fetchObject("company.buzzwords");
        List<String> buzzwords = new ArrayList<String>();
        for (List<String> buzzwordList : buzzwordLists) {
            buzzwords.addAll(buzzwordList);
        }
        return buzzwords.get(genrater.random().nextInt(buzzwords.size()));
    }

    /**
     * Generate a buzzword-laden catch phrase.
     */
    public String catchPhrase() {
        @SuppressWarnings("unchecked")
        List<List<String>> catchPhraseLists = (List<List<String>>) genrater.fakeValuesService().fetchObject("company.buzzwords");
        return joinSampleOfEachList(catchPhraseLists, " ");
    }

    /**
     * When a straight answer won't do, BS to the rescue!
     */
    public String bs() {
        @SuppressWarnings("unchecked")
        List<List<String>> buzzwordLists = (List<List<String>>) genrater.fakeValuesService().fetchObject("company.bs");
        return joinSampleOfEachList(buzzwordLists, " ");
    }

    /**
     * Generate a random company logo url in PNG format.
     */
    public String logo() {
        int number = genrater.random().nextInt(13) + 1;
        return "https://pigment.github.io/fake-logos/logos/medium/color/" + number + ".png";
    }

    public String url() {
        return join(new Object[]{
                "www",
                ".",
                FakerIDN.toASCII(domainName()),
                ".",
                domainSuffix()
        });
    }

    private String domainName(){
        return StringUtils.deleteWhitespace(name().toLowerCase().replaceAll(",", "").replaceAll("'", ""));
    }

    private String domainSuffix() {
        return genrater.fakeValuesService().resolve("internet.domain_suffix", this, genrater);
    }

    private String joinSampleOfEachList(List<List<String>> listOfLists, String separator) {
        List<String> words = new ArrayList<String>();
        for (List<String> list : listOfLists) {
           words.add(list.get(genrater.random().nextInt(list.size())));
        }
        return join(words, separator);
    }
}
