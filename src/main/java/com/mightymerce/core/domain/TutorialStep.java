package com.mightymerce.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A TutorialStep.
 */
@Entity
@Table(name = "TUTORIALSTEP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TutorialStep implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(max = 50)        
    @Column(name = "step", length = 50, nullable = false)
    private String step;
    
    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TutorialStep tutorialStep = (TutorialStep) o;

        if ( ! Objects.equals(id, tutorialStep.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TutorialStep{" +
                "id=" + id +
                ", step='" + step + "'" +
                ", completed='" + completed + "'" +
                '}';
    }
}
