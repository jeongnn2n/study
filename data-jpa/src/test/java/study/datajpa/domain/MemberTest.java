package study.datajpa.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.datajpa.repository.MemberRepository;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired MemberRepository memberRepository;
	
	//@Test
	void testEntity() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);
		
		Member member1 = new Member("member1",10,teamA);
		Member member2 = new Member("member2",20,teamA);
		Member member3 = new Member("member3",30,teamB);
		Member member4 = new Member("member4",40,teamB);
		
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
		
		em.flush();
		em.clear();
		
		List<Member> members = em.createQuery("select m from Member m",Member.class).getResultList();
		
		//출력하지 않고 assert를 사용해도 된다. 하지만 눈으로 쿼리를 확인(공부)하기 위해서!
		for(Member mem : members) {
			System.out.println("member = " + mem);
			System.out.println("->member.team = " + mem.getTeam());
		}
	}
	
	//@Test
	public void jpaEventBaseEntity() throws Exception {
		//given
		Member member = new Member("memeber1");
		memberRepository.save(member);
		
		Thread.sleep(100);
		member.setUsername("member2");
		
		em.flush();
		em.clear();
		
		//when
		Member findMember = memberRepository.findById(member.getId()).get();
		
		//thne
		System.out.println("findMember.createdDate = " + findMember.getCreatedDate());
		//System.out.println("findMember.updatedDate = " + findMember.getUpdatedDate());
		
	}
	
	@Test
	public void eventBaseEntity() throws Exception {
		//given
		Member member = new Member("memeber1");
		memberRepository.save(member);
		
		Thread.sleep(100);
		member.setUsername("member2");
		
		em.flush();
		em.clear();
		
		//when
		Member findMember = memberRepository.findById(member.getId()).get();
		
		//thne
		System.out.println("findMember.createdDate = " + findMember.getCreatedDate());
		System.out.println("findMember.lastmodifiedDate = " + findMember.getLastModifiedDate());
		
	}

}
