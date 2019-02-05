package com.snake.genrater;

import java.util.List;

public class Options {
    private final Genrater genrater;

    protected Options(Genrater genrater) {
        this.genrater = genrater;
    }

    /**
     * Returns a random element from an varargs.
     *
     * @param options The varargs to take a random element from.
     * @param <E>   The type of the elements in the varargs.
     * @return A randomly selected element from the varargs.
     */
    public <E> E option(E... options) {
        return options[genrater.random().nextInt(options.length)];
    }

    /**
     * Returns a random element from Enum.
     *
     * @param enumeration The Enum to take a random element from.
     * @return A randomly selected element from the enum.
     */
    public <E extends Enum<E>> E option(Class<E> enumeration) {
        E[] enumConstants = enumeration.getEnumConstants();
        return enumConstants[genrater.random().nextInt(enumConstants.length)];
    }

    /**
     * Returns a random element from an array.
     *
     * @param array The array to take a random element from.
     * @param <E>   The type of the elements in the array.
     * @return A randomly selected element from the array.
     */
    public <E> E nextElement(E[] array) {
        return array[genrater.random().nextInt(array.length)];
    }

    /**
     * Returns a random element from a list.
     *
     * @param list The list to take a random element from.
     * @param <E>  The type of the elements in the list.
     * @return A randomly selected element from the list.
     */
    public <E> E nextElement(List<E> list) {
        return list.get(genrater.random().nextInt(list.size()));
    }
}
