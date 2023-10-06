package com.pangpang.airbank.global.common.api.nh.domain;

import com.pangpang.airbank.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  NH API의 설정 값을 위한 Entity
 */
@Entity(name = "nh_api_management")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhApiManagement extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column
	private Long isTuno;

	public void updateIsTuno() {
		this.isTuno = this.isTuno + 1;
	}
}
