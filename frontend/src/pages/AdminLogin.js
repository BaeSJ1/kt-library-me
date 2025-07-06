import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authorAPI } from '../services/api';
import styles from '../styles/AdminLogin.module.css';

const AdminLogin = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
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
      // Author API를 통한 로그인 요청
      const loginResponse = await authorAPI.login({
        email: formData.email,
        password: formData.password
      });
      
      console.log('관리자 로그인 성공:', loginResponse);
      
      // 관리자 권한 확인
      if (!loginResponse.isAdmin) {
        throw new Error('관리자 계정이 아닙니다.');
      }
      
      // 사용자 데이터 구성
      const userData = {
        id: loginResponse.id,
        name: loginResponse.name,
        email: loginResponse.email,
        role: 'admin',
        introduction: loginResponse.introduction,
        isApproved: loginResponse.isApproved,
        isAdmin: loginResponse.isAdmin,
        createdAt: loginResponse.createdAt,
        updatedAt: loginResponse.updatedAt
      };

      // 로컬 스토리지에 사용자 정보 저장
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('token', loginResponse.token);

      // 관리자 대시보드로 리다이렉트
      navigate('/admin/authors');
      
      // 페이지 새로고침으로 네비게이션 업데이트
      window.location.reload();
      
    } catch (error) {
      console.error('관리자 로그인 실패:', error);
      
      let errorMessage = '로그인 중 오류가 발생했습니다.';
      
      if (error.message.includes('등록되지 않은 이메일')) {
        errorMessage = '등록되지 않은 이메일입니다.';
      } else if (error.message.includes('비밀번호가 일치하지 않습니다')) {
        errorMessage = '비밀번호가 일치하지 않습니다.';
      } else if (error.message.includes('관리자 계정이 아닙니다')) {
        errorMessage = '관리자 계정이 아닙니다. 관리자 권한이 있는 계정으로 로그인해주세요.';
      } else if (error.message.includes('서버에 연결할 수 없습니다')) {
        errorMessage = 'Author 서비스에 연결할 수 없습니다.\n서버가 실행 중인지 확인해주세요.\n\n🔧 로컬 환경: http://localhost:8083\n🐳 Docker 환경: author 컨테이너 확인';
      }
      
      setErrors({
        submit: errorMessage
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.adminLoginContainer}>
      <div className={styles.adminLoginCard}>
        <div className={styles.adminLoginHeader}>
          <div className={styles.adminLoginLogo}>
            <span className="material-icons">admin_panel_settings</span>
            <h1>관리자 로그인</h1>
          </div>
          <p className={styles.adminLoginSubtitle}>
            KT Library 관리자 시스템에 로그인하세요
          </p>
        </div>

        <form className={styles.adminLoginForm} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label htmlFor="email" className={styles.formLabel}>
              <span className="material-icons">email</span>
              관리자 이메일
            </label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={`${styles.formInput} ${errors.email ? styles.error : ''}`}
              placeholder="관리자 이메일을 입력하세요"
              disabled={isLoading}
            />
            {errors.email && <span className={styles.errorMessage}>{errors.email}</span>}
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="password" className={styles.formLabel}>
              <span className="material-icons">lock</span>
              관리자 비밀번호
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className={`${styles.formInput} ${errors.password ? styles.error : ''}`}
              placeholder="관리자 비밀번호를 입력하세요"
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
            className={`${styles.adminLoginButton} ${isLoading ? styles.loading : ''}`}
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
                관리자 로그인
              </>
            )}
          </button>
        </form>

        <div className={styles.adminDemo}>
          <p className={styles.demoTitle}>데모 관리자 계정:</p>
          <div className={styles.demoAccount}>
            <strong>🔐 관리자:</strong> admin@kt.com / admin123
          </div>
        </div>

        <div className={styles.adminLoginFooter}>
          <p>
            일반 사용자이신가요?{' '}
            <Link to="/login" className={styles.userLoginLink}>
              일반 로그인
            </Link>
            {' '} | {' '}
            <Link to="/" className={styles.homeLink}>
              홈으로
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default AdminLogin;