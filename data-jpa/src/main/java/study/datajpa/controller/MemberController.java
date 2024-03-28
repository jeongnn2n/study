package study.datajpa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import study.datajpa.DTO.MemberDTO;
import study.datajpa.domain.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;

	@GetMapping("/members")
	public Page<MemberDTO> list(@PageableDefault(size = 5) Pageable pageable) {
		return memberRepository.findAll(pageable).map(MemberDTO::new);

	}

	//@PostConstruct
	public void init() {
		for (int i = 0; i < 30; i++) {
			memberRepository.save(new Member("member" + i, i));

		}
	}

}
