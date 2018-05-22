package org.dreamscale.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class RequiresNewTransactor {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void perform(UnitOfWork unitOfWork) {
        unitOfWork.execute();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T performAndReturn(UnitOfWorkWithResponse<T> unitOfWork) {
        return unitOfWork.execute();
    }

}
