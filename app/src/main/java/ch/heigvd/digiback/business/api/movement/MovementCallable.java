package ch.heigvd.digiback.business.api.movement;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.movement.Movement;

public class MovementCallable implements CustomCallable<Movement> {
    protected final String movementsURL = "https://localhost:8080/movement/user/"; // TODO set to backend link
    @Override
    public void setDataAfterLoading(Movement result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Movement call() throws Exception {
        return null;
    }
}
