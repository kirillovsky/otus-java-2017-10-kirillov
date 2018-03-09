package ru.otus.kirillov.view;

public enum View {

    MAIN("main.html"),
    CACHE_STATS("cache-stats.html"),
    ERROR("error.html");

    private String templatePath;

    View(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}
