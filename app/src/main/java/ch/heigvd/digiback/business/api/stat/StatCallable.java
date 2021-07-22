package ch.heigvd.digiback.business.api.stat;


import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Stat;

public class StatCallable implements CustomCallable<Stat> {
    @Override
    public void setDataAfterLoading(Stat stat) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Stat call() throws Exception {
        return null;
    }
}
