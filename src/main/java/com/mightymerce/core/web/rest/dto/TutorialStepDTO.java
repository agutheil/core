package com.mightymerce.core.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TutorialStep entity.
 */
public class TutorialStepDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String step;

    private Boolean completed;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TutorialStepDTO tutorialStepDTO = (TutorialStepDTO) o;

        if ( ! Objects.equals(id, tutorialStepDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TutorialStepDTO{" +
                "id=" + id +
                ", step='" + step + "'" +
                ", completed='" + completed + "'" +
                '}';
    }
}
