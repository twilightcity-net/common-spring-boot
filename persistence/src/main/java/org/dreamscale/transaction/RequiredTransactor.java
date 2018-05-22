package org.dreamscale.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class RequiredTransactor {

    @Transactional(propagation = Propagation.REQUIRED)
    public void perform(UnitOfWork unitOfWork) {
        unitOfWork.execute();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T performAndReturn(UnitOfWorkWithResponse<T> unitOfWork) {
        return unitOfWork.execute();
    }

}
