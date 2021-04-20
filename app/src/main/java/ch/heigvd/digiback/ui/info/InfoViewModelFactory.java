package ch.heigvd.digiback.ui.info;

import android.app.Application;

public class InfoViewModelFactory {

    public InfoViewModelFactory(Application application, Article article, String token) {

    }
}

class QuestionViewModelFactory(
        private val application: Application,
        private val question : Question,
        private val token : String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val constructors = modelClass.declaredConstructors
        return constructors[0].newInstance(application, question, token) as T
    }
}
