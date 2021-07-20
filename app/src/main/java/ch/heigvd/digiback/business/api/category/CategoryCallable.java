package ch.heigvd.digiback.business.api.category;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.category.Category;

public class CategoryCallable implements CustomCallable<Category> {
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
