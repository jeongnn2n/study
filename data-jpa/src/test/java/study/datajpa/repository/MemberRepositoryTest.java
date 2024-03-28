package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.datajpa.DTO.MemberDTO;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;

	@PersistenceContext
	EntityManager em;

	//@Test
	public void findMemberDTOTest() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.setTeam(team);
		memberRepository.save(m1);

		List<MemberDTO> dto = memberRepository.findMemberDTO();
		for (MemberDTO d : dto) {
			System.out.println("dto = " + d);
		}

	}

	//@Test
	public void page() throws Exception {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		// when
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
		// Page<Member> page = memberRepository.findByAge(10, pageRequest);
		Page<Member> page = memberRepository.findByAge(10, pageRequest);

		Page<MemberDTO> toMap = page.map(member -> new MemberDTO(member.getId(), member.getUsername(), null));

		// then
		List<Member> content = page.getContent();// 조회된 데이터
		assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
		assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
		assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
		assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
		assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가
		assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가
	}

	//@Test
	public void bulkUpdate() throws Exception {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		// when
		int resultCount = memberRepository.bulkAgePlus(20);
		em.flush();
		em.clear();

		List<Member> result = memberRepository.findByUsername("member5");
		Member mem5 = result.get(0);
		System.out.println("member5 = " + mem5);
		// then
		// assertThat(resultCount).isEqualTo(3);
	}

	//@Test
	public void findMemberLazy() {
		// member1은 teamA를 참조
		// member2는 teamB를 참조
		// 지연로딩

		// given
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);
		memberRepository.save(new Member("member1", 10, teamA));
		memberRepository.save(new Member("member2", 20, teamB));

		em.flush();
		em.clear();

		// when
		// select Member
		List<Member> members = memberRepository.findAll();

		// then
		for (Member m : members) {
			System.out.println("member = " + m.getUsername());
			System.out.println("team = " + m.getTeam().getClass());
			System.out.println("member.team = " + m.getTeam().getName());
		}
	}

	//@Test
	public void findMemberFetchJoinTest() {
		// given
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);
		memberRepository.save(new Member("member1", 10, teamA));
		memberRepository.save(new Member("member2", 20, teamB));

		em.flush();
		em.clear();

		// when
		// select Member
		List<Member> members = memberRepository.findMemberFatchJoin();

		// then
		for (Member m : members) {
			System.out.println("member = " + m.getUsername());
			System.out.println("team = " + m.getTeam().getClass());
			System.out.println("member.team = " + m.getTeam().getName());
		}
	}
	
	//@Test
	public void queryHint() throws Exception {
		//given
		Member member1 = new Member("member1",10);
		memberRepository.save(member1);
		em.flush();
		em.clear();
		//when
		Member findMember = memberRepository.findReadOnlyByUsername("member1");
		findMember.setUsername("member2");
		
		em.flush();
		
	}
	
	//@Test
	public void lock() {
		//given
		Member member1 = new Member("member1",10);
		memberRepository.save(member1);
		em.flush();
		em.clear();
		
		//when
		List<Member> result = memberRepository.findLockByUsername("member1");
		
	}
	
	//@Test
	public void callCustom() {
		List<Member> resujlt = memberRepository.findMemberCustom();
	}
	
	@Test
	public void nativeQuery() {
		Team teamA = new Team("teamA");
		em.persist(teamA);
		
		Member m1 = new Member("m1",0,teamA);
		Member m2 = new Member("m2",0,teamA);
		em.persist(m1);
		em.persist(m2);
		
		em.flush();
		em.clear();
		
		Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
		
		List<MemberProjection> content = result.getContent();
		for(MemberProjection mp : content) {
			System.out.println("memberProjection = " + mp.getUsername());
			System.out.println("memberProjection = " + mp.getTeamName());
			
		}
	}
}
