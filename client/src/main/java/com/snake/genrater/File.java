package com.snake.genrater;

public class File {
    private final Genrater genrater;

    protected File(Genrater genrater) {
        this.genrater = genrater;
    }

    public String extension() {
        return genrater.resolve("file.extension");
    }
    
    public String mimeType() {
        return genrater.resolve("file.mime_type");
    }

    public String fileName() {
        return fileName(null, null, null, null);
    }

    public String fileName(String dirOrNull, String nameOrNull, String extensionOrNull, String separatorOrNull) {
        final String sep = separatorOrNull == null ? System.getProperty("file.separator") : separatorOrNull;
        final String dir = dirOrNull == null ? genrater.internet().slug() : dirOrNull;
        final String name = nameOrNull == null ? genrater.lorem().word().toLowerCase() : nameOrNull;
        final String ext = extensionOrNull == null ? extension() : extensionOrNull;
        return new StringBuilder(dir).append(sep).append(name).append(".").append(ext).toString();
    }
}
