package net.twilightcity.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MandatoryTransactor {

    @Transactional(propagation = Propagation.MANDATORY)
    public void perform(UnitOfWork unitOfWork) {
        unitOfWork.execute();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public <T> T performAndReturn(UnitOfWorkWithResponse<T> unitOfWork) {
        return unitOfWork.execute();
    }

}
