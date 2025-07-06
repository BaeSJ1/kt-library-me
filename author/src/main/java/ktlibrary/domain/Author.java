package ktlibrary.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlibrary.AuthorApplication;
import lombok.Data;

@Entity
@Table(name = "Author_table")
@Data
//<<< DDD / Aggregate Root
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String introduction;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isApproved = false;

    @Column(nullable = false)
    private Boolean isAdmin = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PostPersist
    protected void afterCreate() {
        // 새로 등록된 작가인 경우 이벤트 발행
        if (this.isNewlyRegistered) {
            AuthorRegistered authorRegistered = new AuthorRegistered(this);
            authorRegistered.publish();  // 즉시 발행 (이미 저장됨)
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public static AuthorRepository repository() {
        AuthorRepository authorRepository = AuthorApplication.applicationContext.getBean(
            AuthorRepository.class
        );
        return authorRepository;
    }

    // 새로 등록된 작가인지 확인하는 플래그
    @Transient
    private boolean isNewlyRegistered = false;

    //<<< Clean Arch / Port Method
    public void registerAuthor(RegisterAuthorCommand registerAuthorCommand) {
        // 작가 등록 로직
        if (registerAuthorCommand.getEmail() == null || registerAuthorCommand.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        if (registerAuthorCommand.getName() == null || registerAuthorCommand.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        if (repository().findByEmail(registerAuthorCommand.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        this.email = registerAuthorCommand.getEmail().trim();
        this.name = registerAuthorCommand.getName().trim();
        this.introduction = registerAuthorCommand.getIntroduction() != null ? registerAuthorCommand.getIntroduction().trim() : "";
        this.password = registerAuthorCommand.getPassword() != null ? registerAuthorCommand.getPassword() : "defaultPassword";
        this.isApproved = false;
        this.isAdmin = false;  // 일반 작가는 관리자가 아님
        this.isNewlyRegistered = true;  // 새로 등록됨을 표시

        // 이벤트 발행은 @PostPersist에서 처리
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void approveAuthor(ApproveAuthorCommand approveAuthorCommand) {
        // 작가 승인 로직
        if (this.isApproved) {
            throw new IllegalStateException("이미 승인된 작가입니다.");
        }

        this.isApproved = true;

        AuthorApproved authorApproved = new AuthorApproved(this);
        authorApproved.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void disapproveAuthor(DisapproveAuthorCommand disapproveAuthorCommand) {
        // 작가 승인 거부 로직
        if (!this.isApproved) {
            throw new IllegalStateException("이미 승인되지 않은 상태입니다.");
        }

        this.isApproved = false;
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void editAuthor(EditAuthorCommand editAuthorCommand) {
        // 작가 정보 수정 로직
        if (editAuthorCommand.getEmail() != null && !editAuthorCommand.getEmail().trim().isEmpty()) {
            if (this.email.equals(editAuthorCommand.getEmail().trim())) {
                throw new IllegalArgumentException("기존과 동일한 이메일입니다.");
            }

            if (repository().findByEmail(editAuthorCommand.getEmail().trim()).isPresent()) {
                throw new IllegalArgumentException("이미 등록된 이메일입니다.");
            }

            this.email = editAuthorCommand.getEmail().trim();
        }

        if (editAuthorCommand.getIntroduction() != null) {
            this.introduction = editAuthorCommand.getIntroduction().trim();
        }

        AuthorEdited authorEdited = new AuthorEdited(this);
        authorEdited.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public boolean isAdminUser() {
        return this.isAdmin != null && this.isAdmin;
    }
    
    public boolean validatePassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
    
    public static LoginResponse login(LoginCommand loginCommand, AuthorRepository repository) {
        // 이메일로 사용자 찾기
        java.util.Optional<Author> authorOpt = repository.findByEmail(loginCommand.getEmail());
        
        if (authorOpt.isEmpty()) {
            throw new IllegalArgumentException("등록되지 않은 이메일입니다.");
        }
        
        Author author = authorOpt.get();
        
        // 비밀번호 검증
        if (!author.validatePassword(loginCommand.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        // 임시 토큰 생성 (실제로는 JWT 등을 사용)
        String token = "temp_token_" + author.getId() + "_" + System.currentTimeMillis();
        
        String message = author.isAdminUser() ? "관리자 로그인 성공" : "작가 로그인 성공";
        
        return new LoginResponse(author, token, message);
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
