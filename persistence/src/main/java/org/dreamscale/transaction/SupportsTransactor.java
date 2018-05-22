package org.dreamscale.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class SupportsTransactor {

    @Transactional(propagation = Propagation.SUPPORTS)
    public void perform(UnitOfWork unitOfWork) {
        unitOfWork.execute();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public <T> T performAndReturn(UnitOfWorkWithResponse<T> unitOfWork) {
        return unitOfWork.execute();
    }

}
