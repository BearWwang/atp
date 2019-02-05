package com.snake.genrater;

public class Witcher {
    private final Genrater genrater;
    
    protected Witcher(Genrater genrater) {
        this.genrater = genrater;
    }
    
    public String character() {
        return genrater.fakeValuesService().resolve("witcher.characters", this, genrater);
    }
    
    public String witcher() {
        return genrater.fakeValuesService().resolve("witcher.witchers", this, genrater);
    }
    
    public String school() {
        return genrater.fakeValuesService().resolve("witcher.schools", this, genrater);
    }
    
    public String location() {
        return genrater.fakeValuesService().resolve("witcher.locations", this, genrater);
    }
    
    public String quote() {
        return genrater.fakeValuesService().resolve("witcher.quotes", this, genrater);
    }
    
    public String monster() {
        return genrater.fakeValuesService().resolve("witcher.monsters", this, genrater);
    }
}
