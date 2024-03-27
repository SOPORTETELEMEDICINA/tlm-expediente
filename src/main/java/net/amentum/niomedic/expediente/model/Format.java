package net.amentum.niomedic.expediente.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.soap.Text;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  by marellano on 14/06/17.
 */
@Entity
@Table
@TypeDef(
		name = "jsonb", typeClass = JsonBinaryType.class
	)
public class Format implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormat;
    @NotEmpty(message = "Es requerido el titulo del formato")
    private String title;
    @NotNull(message = "Es requerido el estatus del formato.")
    private Boolean active = Boolean.TRUE;
    private Integer version;
    
    @NotNull(message = "Es requerido el json.")
    @Type( type = "jsonb" )
	@Column(columnDefinition = "jsonb")
	private JsonNode jsonFormat;
    @NotEmpty(message = "Es requerido el tipo de formato.")
    private String formatType;

    @OneToMany(mappedBy = "format", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FormatResult> formatResults = new ArrayList<>();

    /**
     *
     * @return
     */
    public Long getIdFormat() {
        return idFormat;
    }

    /**
     *
     * @param idFormat
     */
    public void setIdFormat(Long idFormat) {
        this.idFormat = idFormat;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public Boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    /**
     *
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     *
     * @return
     */
    public JsonNode getJsonFormat() {
        return jsonFormat;
    }

    /**
     *
     * @param jsonFormat
     */
    public void setJsonFormat(JsonNode jsonFormat) {
        this.jsonFormat = jsonFormat;
    }

    /**
     *
     * @return
     */
    public String getFormatType() {
        return formatType;
    }

    /**
     *
     * @param formatType
     */
    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    /**
     *
     * @return
     */
    public List<FormatResult> getFormatResults() {
        return formatResults;
    }

    /**
     *
     * @param formatResults
     */
    public void setFormatResults(List<FormatResult> formatResults) {
        this.formatResults = formatResults;
    }

    @Override
    public String toString() {
        return "Format{" +
                "idFormat=" + idFormat +
                ", title='" + title + '\'' +
                ", active=" + active +
                ", version=" + version +
                ", jsonFormat='" + jsonFormat + '\'' +
                ", formatType='" + formatType + '\'' +
                '}';
    }

    /**
     *
     * @return
     */
    public String toStringResume() {
        return "Format{" +
                "idFormat=" + idFormat +
                ", title='" + title + '\'' +
                ", formatType='" + formatType + '\'' +
                '}';
    }
}
