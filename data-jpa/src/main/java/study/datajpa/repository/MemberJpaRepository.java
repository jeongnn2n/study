package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.datajpa.domain.Member;

@Repository
public class MemberJpaRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public Member save(Member member) {
		em.persist(member);
		return member;
	}
	
	public void delete(Member member) {
		em.remove(member);
	}
	
	public Member find(Long id) {
		return em.find(Member.class, id);
	}
	
	public List<Member> findAll(){
		return em.createQuery("select m from Member m",Member.class).getResultList();
	}
	
	public Optional<Member> findById(Long id){
		Member member = em.find(Member.class, id);
		return Optional.ofNullable(member);
	}
	
	public Long count() {
		return em.createQuery("select count(m) from Member m",Long.class).getSingleResult();
	}

}
