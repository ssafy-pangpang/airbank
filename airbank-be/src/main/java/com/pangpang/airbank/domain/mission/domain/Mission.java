package com.pangpang.airbank.domain.mission.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.global.meta.converter.MissionStatusConverter;
import com.pangpang.airbank.global.meta.domain.MissionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "mission")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mission extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column
	private String title;

	@Size(max = 255)
	@Column
	private Long content;

	@NotNull
	@Column
	private Long amount;

	@NotNull
	@Column
	private Integer creditScore;

	@Size(max = 6)
	@NotNull
	@Column
	private String color;

	@NotNull
	@Column
	private LocalDateTime expiredAt;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id",
		foreignKey = @ForeignKey(name = "fk_mission_to_group_group_id"))
	private Group group;

	@NotNull
	@Builder.Default
	@ColumnDefault("'PENDING'")
	@Column(length = 20)
	@Convert(converter = MissionStatusConverter.class)
	private MissionStatus status = MissionStatus.PENDING;
}
