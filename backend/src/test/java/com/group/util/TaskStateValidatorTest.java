package com.group.util;

import com.group.enumeration.StateEnum;
import org.junit.Assert;
import org.junit.Test;

public class TaskStateValidatorTest {
    TaskStateValidator stateValidator = new TaskStateValidator();

    @Test
    public void validateTaskStateNew() {
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.NEW, StateEnum.COMPLETED));
        Assert.assertTrue(stateValidator.validateTaskState(StateEnum.NEW, StateEnum.CLOSED));
        Assert.assertTrue(stateValidator.validateTaskState(StateEnum.NEW, StateEnum.IN_PROGRESS));
    }

    @Test
    public void validateTaskStateInProgress() {
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.IN_PROGRESS, StateEnum.NEW));
        Assert.assertTrue(stateValidator.validateTaskState(StateEnum.IN_PROGRESS, StateEnum.CLOSED));
        Assert.assertTrue(stateValidator.validateTaskState(StateEnum.IN_PROGRESS, StateEnum.COMPLETED));
    }

    @Test
    public void validateTaskStateCompleted() {
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.COMPLETED, StateEnum.NEW));
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.COMPLETED, StateEnum.IN_PROGRESS));
        Assert.assertTrue(stateValidator.validateTaskState(StateEnum.COMPLETED, StateEnum.CLOSED));
    }

    @Test
    public void validateTaskStateClosed() {
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.CLOSED, StateEnum.NEW));
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.CLOSED, StateEnum.IN_PROGRESS));
        Assert.assertFalse(stateValidator.validateTaskState(StateEnum.CLOSED, StateEnum.COMPLETED));
    }


}