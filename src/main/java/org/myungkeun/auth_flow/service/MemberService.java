package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.request.UpdateInfoRequest;
import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.entity.Member;

import java.security.Principal;

public interface MemberService {
    Member getMemberInfo(Principal connectedMember);

    Member updateMemberPassword(Principal connectedMember, UpdatePasswordRequest request);

    Boolean existsEmail(String email);

    Boolean existsNickname(String nickname);

    Member updateMemberInfo(Principal connectedMember, UpdateInfoRequest request);
}
