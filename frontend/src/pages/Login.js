import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authorAPI, customerAPI } from '../services/api';
import styles from '../styles/Login.module.css';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    role: '',
    email: '',
    password: ''
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // 에러 메시지 클리어
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.role) {
      newErrors.role = '로그인 유형을 선택해주세요.';
    }

    if (!formData.email.trim()) {
      newErrors.email = '이메일을 입력해주세요.';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = '올바른 이메일 형식을 입력해주세요.';
    }

    if (!formData.password.trim()) {
      newErrors.password = '비밀번호를 입력해주세요.';
    } else if (formData.password.length < 6) {
      newErrors.password = '비밀번호는 6자 이상이어야 합니다.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      let loginResponse;
      let userData;

      if (formData.role === 'customer') {
        // Customer API를 통한 로그인 요청
        loginResponse = await customerAPI.login({
          email: formData.email,
          password: formData.password
        });

        console.log('고객 로그인 성공:', loginResponse);

        // 사용자 데이터 구성
        userData = {
          id: loginResponse.id,
          name: loginResponse.name,
          email: loginResponse.email,
          role: 'customer',
          isKtUser: loginResponse.isKtUser,
          createdAt: loginResponse.createdAt,
          updatedAt: loginResponse.updatedAt
        };
      } else {
        // Author API를 통한 로그인 요청
        loginResponse = await authorAPI.login({
          email: formData.email,
          password: formData.password
        });

        console.log('작가/관리자 로그인 성공:', loginResponse);

        // 사용자 데이터 구성
        userData = {
          id: loginResponse.id,
          name: loginResponse.name,
          email: loginResponse.email,
          role: loginResponse.isAdmin ? 'admin' : 'author',
          introduction: loginResponse.introduction,
          isApproved: loginResponse.isApproved,
          isAdmin: loginResponse.isAdmin,
          createdAt: loginResponse.createdAt,
          updatedAt: loginResponse.updatedAt
        };
      }

      // 선택한 역할과 실제 권한 확인
      if (formData.role === 'customer' && userData.role !== 'customer') {
        throw new Error('고객 계정이 아닙니다.');
      }
      
      if (formData.role === 'author' && userData.role === 'admin') {
        throw new Error('관리자 계정입니다. 관리자 로그인을 이용해주세요.');
      }

      if (formData.role === 'author' && userData.role === 'customer') {
        throw new Error('고객 계정입니다. 고객 로그인을 이용해주세요.');
      }

      // 로컬 스토리지에 사용자 정보 저장
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('token', loginResponse.token);

      // 역할에 따른 리다이렉트
      switch (userData.role) {
        case 'customer':
          navigate('/customer/books');
          break;
        case 'author':
          navigate('/author/manuscripts');
          break;
        case 'admin':
          navigate('/admin/authors');
          break;
        default:
          navigate('/');
      }

      // 페이지 새로고침으로 네비게이션 업데이트
      window.location.reload();
      
    } catch (error) {
      console.error('로그인 실패:', error);
      
      let errorMessage = '로그인 중 오류가 발생했습니다.';
      
      if (error.message.includes('등록되지 않은 이메일')) {
        errorMessage = '등록되지 않은 이메일입니다.';
      } else if (error.message.includes('비밀번호가 일치하지 않습니다')) {
        errorMessage = '비밀번호가 일치하지 않습니다.';
      } else if (error.message.includes('승인 대기 중인 계정')) {
        errorMessage = '승인 대기 중인 계정입니다. 관리자 승인을 기다려주세요.';
      } else if (error.message.includes('서버에 연결할 수 없습니다')) {
        errorMessage = 'Author 서비스에 연결할 수 없습니다.\n서버가 실행 중인지 확인해주세요.\n\n🔧 로컬 환경: http://localhost:8083\n🐳 Docker 환경: author 컨테이너 확인';
      } else if (error.message.includes('고객 계정이 아닙니다') || error.message.includes('관리자 계정입니다')) {
        errorMessage = error.message;
      }
      
      setErrors({
        submit: errorMessage
      });
    } finally {
      setIsLoading(false);
    }
  };

  const getRoleDisplayName = (role) => {
    const roleNames = {
      author: '작가',
      customer: '고객'
    };
    return roleNames[role] || role;
  };

  return (
    <div className={styles.loginContainer}>
      <div className={styles.loginCard}>
        <div className={styles.loginHeader}>
          <div className={styles.loginLogo}>
            <span className="material-icons">menu_book</span>
            <h1>KT Library</h1>
          </div>
          <p className={styles.loginSubtitle}>도서 출판 및 구독 플랫폼에 로그인하세요</p>
        </div>

        <form className={styles.loginForm} onSubmit={handleSubmit}>
          {/* 역할 선택 섹션 */}
          <div className={styles.formGroup}>
            <label className={styles.formLabel}>
              <span className="material-icons">account_circle</span>
              로그인 유형
            </label>
            <div className={styles.roleSelection}>
              <div 
                className={`${styles.roleOption} ${formData.role === 'customer' ? styles.selected : ''}`}
                onClick={() => handleChange({ target: { name: 'role', value: 'customer' } })}
              >
                <input
                  type="radio"
                  name="role"
                  value="customer"
                  checked={formData.role === 'customer'}
                  onChange={handleChange}
                  className={styles.roleInput}
                />
                <div className={styles.roleContent}>
                  <span className="material-icons">person</span>
                  <span>고객</span>
                </div>
              </div>
              
              <div 
                className={`${styles.roleOption} ${formData.role === 'author' ? styles.selected : ''}`}
                onClick={() => handleChange({ target: { name: 'role', value: 'author' } })}
              >
                <input
                  type="radio"
                  name="role"
                  value="author"
                  checked={formData.role === 'author'}
                  onChange={handleChange}
                  className={styles.roleInput}
                />
                <div className={styles.roleContent}>
                  <span className="material-icons">edit</span>
                  <span>작가</span>
                </div>
              </div>
            </div>
            {errors.role && <span className={styles.errorMessage}>{errors.role}</span>}
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="email" className={styles.formLabel}>
              <span className="material-icons">email</span>
              이메일
            </label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={`${styles.formInput} ${errors.email ? styles.error : ''}`}
              placeholder="이메일을 입력하세요"
              disabled={isLoading}
            />
            {errors.email && <span className={styles.errorMessage}>{errors.email}</span>}
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="password" className={styles.formLabel}>
              <span className="material-icons">lock</span>
              비밀번호
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={`${styles.formInput} ${errors.password ? styles.error : ''}`}
              placeholder="비밀번호를 입력하세요"
              disabled={isLoading}
            />
            {errors.password && <span className={styles.errorMessage}>{errors.password}</span>}
          </div>

          {errors.submit && (
            <div className={`${styles.errorMessage} ${styles.submitError}`}>
              <span className="material-icons">error</span>
              {errors.submit}
            </div>
          )}

          <button 
            type="submit" 
            className={`${styles.loginButton} ${isLoading ? styles.loading : ''}`}
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <span className={styles.loadingSpinner}></span>
                로그인 중...
              </>
            ) : (
              <>
                <span className="material-icons">login</span>
                {formData.role ? `${getRoleDisplayName(formData.role)}으로 로그인` : '로그인'}
              </>
            )}
          </button>
        </form>

        <div className={styles.loginDemo}>
          <p className={styles.demoTitle}>데모 계정:</p>
          <div className={styles.demoAccounts}>
            <div className={styles.demoAccount}>
              <strong>👤 고객:</strong> customer@kt.com / password
            </div>
            <div className={styles.demoAccount}>
              <strong>✏️ 작가:</strong> author@kt.com / password
            </div>
          </div>
        </div>

        <div className={styles.loginFooter}>
          <p>
            계정이 없으신가요?{' '}
            <Link to="/register" className={styles.registerLink}>
              회원가입
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;