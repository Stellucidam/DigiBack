package ch.heigvd.digiback.business.api.category;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.category.Category;

public class CategoryCallable implements CustomCallable<Category> {
    protected final String categoriesURL = "https://infomaldedos.ch/wp-json/wp/v2/categories/";

    @Override
    public void setDataAfterLoading(Category category) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Category call() throws Exception {
        return null;
    }
}
