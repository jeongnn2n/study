package study.datajpa.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {
	//등록자
	@CreatedBy
    @Column(updatable = false)
    private String createdBy;

    //수정자
    @LastModifiedBy
    private String lastModifiedBy;
}
