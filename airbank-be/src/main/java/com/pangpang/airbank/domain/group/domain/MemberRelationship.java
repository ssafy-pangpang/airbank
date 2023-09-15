package com.pangpang.airbank.domain.group.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.MemberRole;

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

@Entity(name = "member_relationship")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member_relationship SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class MemberRelationship {
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
	@JoinColumn(name = "child_id", foreignKey = @ForeignKey(name = "fk_member_relationship_to_member_child_id"))
	private Member child;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_member_relationship_to_member_parent_id"))
	private Member parent;

	public static MemberRelationship of(Member parentMember, Member childMember) {
		return MemberRelationship.builder()
			.parent(parentMember)
			.child(childMember)
			.build();
	}

	public Member getPartnerMember(Member member) {
		if (member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			return this.getChild();
		}
		return this.getParent();
	}
}
