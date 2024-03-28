package study.datajpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter//setter는 공부용,사용 지양
@NoArgsConstructor(access = AccessLevel.PROTECTED) //jpa는 기본생성자가 필요, private로 하면 안됨. protected까지만 허용
@ToString(of={"id","username","age"}) //편의를 위함. 연관관계있는 team은 넣으면 안된다. toString호출시 무한루프 될 수 있다.
public class Member  extends BaseTimeEntity {
	
	@Id @GeneratedValue
	@Column(name="member_id")
	private Long id;
	private String username;
	private int age;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="team_id")
	private Team team;
	
	//생성자에 값 세팅하기,setter 쓰지 않기
	public Member(String username) {
		this.username = username;
	}
	
	//연관관계 편의 매소드(양방향 연관관계)
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
	
	//test용(?) 매개변수 3개짜리 생성자
	public Member(String member, int age, Team team) {
		this.username = member;
		this.age = age;
		if(team!=null) { //일단 실행
			changeTeam(team);
		}
	}
	
	public Member(String member, int age) {
		this.username = member;
		this.age = age;
	}

}
