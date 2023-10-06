package com.pangpang.airbank.domain.group.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.pangpang.airbank.domain.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "group")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE group SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean activated = Boolean.FALSE;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean deleted = Boolean.FALSE;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_id", foreignKey = @ForeignKey(name = "fk_group_to_member_child_id"))
	private Member child;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_group_to_member_parent_id"))
	private Member parent;

	public static Group of(Member parentMember, Member childMember) {
		return Group.builder()
			.parent(parentMember)
			.child(childMember)
			.build();
	}

	public Member getPartnerMember(Long memberId) {
		if (this.parent.getId().equals(memberId)) {
			return this.child;
		}
		return this.parent;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
}
