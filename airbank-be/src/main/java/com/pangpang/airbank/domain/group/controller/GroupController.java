package com.pangpang.airbank.domain.group.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.group.dto.CommonFundManagementRequestDto;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PatchConfirmChildRequestDto;
import com.pangpang.airbank.domain.group.dto.PatchFundManagementResponseDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;
import com.pangpang.airbank.domain.group.service.GroupService;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "groups", description = "자녀와 부모의 관계를 관리하는 API")
public class GroupController {
	private final GroupService groupService;

	/**
	 *  그룹 멤버 조회
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @return ResponseEntity<EnvelopeResponse < GetPartnersResponseDto>>
	 * @see GroupService
	 */
	@Operation(summary = "그룹 조회",
		description = "나와 관계가 있는 사용자의 정보를 조회합니다. 자식일때는 부모의 정보가, 부모일때는 자식들의 정보가 조회됩니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "그룹 조회 성공",
			content = @Content(schema = @Schema(implementation = GetPartnersResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
	})
	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetPartnersResponseDto>> getPartners(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {
		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetPartnersResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(groupService.getPartners(authenticatedMemberArgument.getMemberId()))
				.build());
	}

	/**
	 *  자녀 등록
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param postEnrollChildRequestDto PostEnrollChildRequestDto
	 * @return ResponseEntity<EnvelopeResponse < CommonIdResponseDto>>
	 * @see GroupService
	 */
	@Operation(summary = "자녀 등록", description = "부모가 휴대폰 번호로 자녀를 검색해서 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "자녀 등록 요청 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1302", description = "자녀를 등록할 권한이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1501", description = "등록된 휴대폰 번호가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1300", description = "이미 부모가 존재하는 자녀입니다.", content = @Content),
		@ApiResponse(responseCode = "1301", description = "이미 자녀 등록이 진행 중 입니다.", content = @Content),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
	})
	@PostMapping()
	public ResponseEntity<EnvelopeResponse<CommonIdResponseDto>> enrollChild(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostEnrollChildRequestDto postEnrollChildRequestDto) {

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(groupService.enrollChild(authenticatedMemberArgument.getMemberId(), postEnrollChildRequestDto))
				.build());
	}

	/**
	 *  자녀 등록 수락/거절
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param patchConfirmChildRequestDto PatchConfirmRequestDto
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < CommonIdResponseDto>>
	 * @see GroupService
	 */
	@Operation(summary = "자녀 등록 수락/거절", description = "부모가 보낸 그룹 요청을 수락하거나 거절합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "자녀 등록 수락/거절 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1304", description = "자녀만 접근할 수 있습니다.", content = @Content),
		@ApiResponse(responseCode = "1303", description = "등록중인 그룹이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
	})
	@PatchMapping("/confirm")
	public ResponseEntity<EnvelopeResponse<CommonIdResponseDto>> confirmEnrollmentChild(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PatchConfirmChildRequestDto patchConfirmChildRequestDto, @RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(groupService.confirmEnrollmentChild(authenticatedMemberArgument.getMemberId(),
					patchConfirmChildRequestDto, groupId))
				.build());
	}

	/**
	 *  자금 관리 생성
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param commonFundManagementRequestDto CommonFundManagementRequestDto
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < CommonIdResponseDto>>
	 * @see GroupService
	 */
	@Operation(summary = "자금 관리 생성", description = "부모가 자녀를 등록하면 해당 그룹에 대한 자금 관리 데이터를 저장합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "자금 관리 생성",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1201", description = "자금 관리를 수정할 권한이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1305", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1203", description = "자금 관리가 이미 존재합니다.", content = @Content),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
	})
	@PostMapping("/fund")
	public ResponseEntity<EnvelopeResponse<CommonIdResponseDto>> saveFundManagement(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody CommonFundManagementRequestDto commonFundManagementRequestDto,
		@RequestParam("group_id") Long groupId) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(groupService.saveFundManagement(authenticatedMemberArgument.getMemberId(),
					commonFundManagementRequestDto, groupId))
				.build());
	}

	/**
	 *  자금 관리 수정
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param commonFundManagementRequestDto CommonFundManagementRequestDto
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < PatchFundManagementResponseDto>>
	 * @see GroupService
	 */
	@Operation(summary = "자금 관리 수정", description = "부모가 자금 관리를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "자금 관리 생성",
			content = @Content(schema = @Schema(implementation = PatchFundManagementResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1201", description = "자금 관리를 수정할 권한이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1305", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1200", description = "자금 관리를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
	})
	@PatchMapping("/fund")
	public ResponseEntity<EnvelopeResponse<PatchFundManagementResponseDto>> updateFundManagement(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody CommonFundManagementRequestDto commonFundManagementRequestDto,
		@RequestParam("group_id") Long groupId) {
		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchFundManagementResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(groupService.updateFundManagement(authenticatedMemberArgument.getMemberId(),
					commonFundManagementRequestDto,
					groupId))
				.build());
	}
}
