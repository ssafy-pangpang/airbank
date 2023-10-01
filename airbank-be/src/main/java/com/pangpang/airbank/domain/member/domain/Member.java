package com.pangpang.airbank.domain.member.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.auth.dto.PostLoginRequestDto;
import com.pangpang.airbank.global.meta.converter.MemberRoleConverter;
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE frame SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column
	private String oauthIdentifier;

	@Size(max = 32)
	@Column
	private String name;

	@Size(max = 30)
	@Column
	private String phoneNumber;

	@NotNull
	@Builder.Default
	@ColumnDefault("632")
	@Column
	private Integer creditScore = 632;

	@Size(max = 255)
	@Column
	private String imageUrl;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean deleted = Boolean.FALSE;

	@NotNull
	@Builder.Default
	@ColumnDefault("'UNKNOWN'")
	@Column(length = 20)
	@Convert(converter = MemberRoleConverter.class)
	private MemberRole role = MemberRole.UNKNOWN;

	/**
	 * 이름 수정
	 *
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 핸드폰번호 수정
	 *
	 * @param phoneNumber String
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 사용자 타입 수정
	 *
	 * @param role String
	 */
	public void setRole(MemberRole role) {
		this.role = role;
	}

	/**
	 * 신용점수 수정
	 *
	 * @param points Integer
	 */
	public void updateCreditScore(Integer points) {
		if (this.creditScore + points > CreditRating.ONE.getMaxScore()) {
			this.creditScore = CreditRating.ONE.getMaxScore();
			return;
		}
		if (this.creditScore + points < CreditRating.TEN.getMinScore()) {
			this.creditScore = CreditRating.TEN.getMinScore();
			return;
		}
		this.creditScore += points;
	}

	public static Member from(PostLoginRequestDto postLoginRequestDto) {
		return Member.builder()
			.oauthIdentifier(postLoginRequestDto.getOauthIdentifier())
			.imageUrl(getImageUrl(postLoginRequestDto.getImageUrl(),
				postLoginRequestDto.getIsDefaultImage()))
			.build();
	}

	private static String getImageUrl(String profileImageUrl, Boolean isDefaultImage) {
		if (isDefaultImage) {
			return null;
		}
		return profileImageUrl;
	}

}
