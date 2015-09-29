package com.mightymerce.core.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the LegalInfo entity.
 */
public class LegalInfoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String title;

    @NotNull
    @Size(max = 100)
    private String purpose;

    @NotNull
    @Size(max = 50)
    private String pageTitle;

    @NotNull
    private String pageText;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageText() {
        return pageText;
    }

    public void setPageText(String pageText) {
        this.pageText = pageText;
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

        LegalInfoDTO legalInfoDTO = (LegalInfoDTO) o;

        if ( ! Objects.equals(id, legalInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LegalInfoDTO{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", purpose='" + purpose + "'" +
                ", pageTitle='" + pageTitle + "'" +
                ", pageText='" + pageText + "'" +
                '}';
    }
}
