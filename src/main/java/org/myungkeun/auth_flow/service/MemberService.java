package org.myungkeun.auth_flow.service;

import org.myungkeun.auth_flow.dto.request.UpdatePasswordRequest;
import org.myungkeun.auth_flow.entity.Member;

import java.security.Principal;

public interface MemberService {
    Member getMemberInfo(Principal connectedUser);

    Member updateMemberPassword(Principal connectedUser, UpdatePasswordRequest request);

    Boolean existsEmail(String email);

    Boolean existsNickname(String nickname);
}
