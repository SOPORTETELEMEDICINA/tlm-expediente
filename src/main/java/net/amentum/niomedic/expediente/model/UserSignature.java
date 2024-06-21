package net.amentum.niomedic.expediente.model;

import javax.persistence.*;
import java.util.Date;

@Table(name="user_signature")
public class UserSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_signature_id")
    private Long userSignatureId;

    @Column(name = "user_app_id")
    private Long userAppId;

    @Column(name = "signature_name")
    private String signatureName;

    @Column(name = "image_content")
    private byte[] imageContent;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate = new Date();

    public Long getUserAppId() { return this.getUserAppId(); }

    public String getSignatureName() { return this.signatureName; }

    public String getImageContentType() {
        return imageContentType;
    }

    public byte[] getImageContent() { return this.imageContent; }

    public void setUserAppId(Long userAppId) {
        this.userAppId = userAppId;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }
}
