package ch.heigvd.digiback.business.api;

import java.util.concurrent.Callable;

import ch.heigvd.digiback.ui.data.LoginDataSource;
import ch.heigvd.digiback.ui.data.LoginRepository;

public interface CustomCallable<R> extends Callable<R> {
    LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
    void setDataAfterLoading(R result);
    void setUiForLoading();
}
