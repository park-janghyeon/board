package com.study.board.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //insert 됐을 때의 시간을 자동으로 입력해준다
    @Column(updatable = false) // 수정시에는 관여 안함
    private LocalDateTime createdTime;

    @UpdateTimestamp //수정됐을 때의 시간을 자동으로 입력해준다
    @Column(insertable = false) // insert시에는 관여 안함
    private LocalDateTime updatedTime;
}
