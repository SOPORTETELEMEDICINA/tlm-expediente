package net.amentum.niomedic.expediente.model;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


/**
 *  @author  by marellano on 14/06/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@TypeDef(
		name = "jsonb", typeClass = JsonBinaryType.class
	)
public class FormatResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormatResult;
    private String username;
    @NotNull(message = "Es requerido la fecha de creacion.")
    private Timestamp createdDate;
    @NotNull(message = "Es requerido el json.")
    @Type( type = "jsonb" )
	@Column(columnDefinition = "jsonb")
	private JsonNode jsonAnswer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_consulta")
    Consulta consulta;
    

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "id_format",nullable = false)
    private Format format;

    @Override
    public String toString() {
        return "FormatResult{" +
                "idFormatResult=" + idFormatResult +
                ", username='" + username + '\'' +
                ", createdDate=" + createdDate +
                ", format=" + format +
                '}';
    }
}
