package study.datajpa.DTO;

import lombok.Data;
import study.datajpa.domain.Member;

@Data // entity는 @data 쓰면 안된당
public class MemberDTO {

	private Long id;
	private String username;
	private String teamName;

	public MemberDTO(Long id, String username, String teamName) {
		this.id = id;
		this.username = username;
		this.teamName = teamName;
	}
	
	public MemberDTO(Member member) {
		this.id = member.getId();
		this.username = member.getUsername();
	}

}
