package ch.heigvd.digiback.business.api.movement;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.movement.Movement;

public class MovementCallable implements CustomCallable<Movement> {
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
