package ru.otus.kirillov.view;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TemplateEngine {

    private final static String VIEW_PATH_PREFIX = "/pages/";
    private final static String VIEW_FILE_EXTENSION = "";

    private Handlebars handlebars = new Handlebars();

    private Map<View, Template> compiledPages = new HashMap<>();

    public TemplateEngine() {
        TemplateLoader loader = handlebars.getLoader();
        loader.setPrefix(VIEW_PATH_PREFIX);
        loader.setSuffix(VIEW_FILE_EXTENSION);
    }

    public TemplateEngine(List<View> views) {
        this();
        views.forEach(this::initView);
    }

    public void initView(@NotNull View view) {
        compiledPages.computeIfAbsent(view,
                v -> compile(getTemplateSource(v.getTemplatePath()))
        );
    }

    public String getPage(@NotNull View view, @NotNull Map<String, Object> variableMap) {
        if (!compiledPages.containsKey(view)) {
            throw new RuntimeException(
                    String.format("Not found comiled template for view %s", view)
            );
        }
        return safety(() -> compiledPages.get(view).apply(variableMap));
    }

    private TemplateSource getTemplateSource(String location) {
        return safety(
                () -> handlebars.getLoader().sourceAt(location),
                () -> String.format("Find view for location %s failed", location)
        );

    }

    private Template compile(TemplateSource templateSource) {
        return safety(
                () -> handlebars.compile(templateSource),
                () -> String.format("Compilation for template source %s failed", templateSource)
        );
    }

    @FunctionalInterface
    private interface NonSafetySupplier<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Exception;
    }

    private <T> T safety(NonSafetySupplier<T> operation, Supplier<String> errorMsg) {
        try {
            return operation.get();
        } catch (Exception e) {
            throw new RuntimeException(errorMsg.get(), e);
        }
    }

    private <T> T safety(NonSafetySupplier<T> operation) {
        try {
            return operation.get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
