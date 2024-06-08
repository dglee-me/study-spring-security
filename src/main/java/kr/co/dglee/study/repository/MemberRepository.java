package kr.co.dglee.study.repository;

import java.util.Optional;
import kr.co.dglee.study.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);
}
