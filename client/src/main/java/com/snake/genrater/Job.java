package com.snake.genrater;

public class Job {

    private final Genrater genrater;

    public Job(final Genrater genrater) {
        this.genrater = genrater;
    }

    public String field() {
        return genrater.fakeValuesService().resolve("job.field", this, genrater);
    }

    public String seniority() {
        return genrater.fakeValuesService().resolve("job.seniority", this, genrater);
    }

    public String position() {
        return genrater.fakeValuesService().resolve("job.position", this, genrater);
    }

    public String keySkills() {
        return genrater.fakeValuesService().resolve("job.key_skills", this, genrater);
    }

    public String title() {
        return genrater.fakeValuesService().resolve("job.title", this, genrater);
    }
}
