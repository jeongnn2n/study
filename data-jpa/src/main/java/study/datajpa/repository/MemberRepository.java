package study.datajpa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import study.datajpa.DTO.MemberDTO;
import study.datajpa.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	@Query("select new study.datajpa.DTO.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDTO> findMemberDTO();

	// 사용자 정의 코드
	Page<Member> findByAge(int age, Pageable pageable);

	@Modifying(clearAutomatically = true)
	@Query("update Member m set m.age = m.age +1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	List<Member> findByUsername(String username);

	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFatchJoin();

	@Override
	@EntityGraph(attributePaths = { "team" })
	List<Member> findAll();

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(String username);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String name);
	
	List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
	
	@Query(value="select * from member where username = ?", nativeQuery = true)
	Member findByNativeQuery(String username);
	
	@Query(value= "select m.member_id as id, m.username, t.name as teamName " + 
				"from member m left join team t", 
				countQuery = "select count(*) from member",
				nativeQuery = true)
	Page<MemberProjection> findByNativeProjection(Pageable pageable);

}
