import React, { useState, useEffect } from 'react';
import { authorAPI } from '../services/api';
import styles from '../styles/AuthorProfile.module.css';

const AuthorProfile = () => {
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setSaving] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [formData, setFormData] = useState({
    email: '',
    introduction: ''
  });
  const [errors, setErrors] = useState({});

  // 컴포넌트 마운트 시 사용자 정보 로드
  useEffect(() => {
    loadUserProfile();
  }, []);

  const loadUserProfile = () => {
    try {
      const userData = JSON.parse(localStorage.getItem('user'));
      if (userData) {
        setUser(userData);
        setFormData({
          email: userData.email || '',
          introduction: userData.introduction || ''
        });
      }
    } catch (error) {
      console.error('사용자 정보 로드 실패:', error);
    } finally {
      setIsLoading(false);
    }
  };

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

    if (formData.introduction && formData.introduction.length > 500) {
      newErrors.introduction = '소개글은 500자 이하로 입력해주세요.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSave = async () => {
    if (!validateForm()) {
      return;
    }

    setSaving(true);
    setSuccessMessage('');

    try {
      // 변경된 필드만 전송
      const updateData = {};
      if (formData.email !== user.email) {
        updateData.email = formData.email;
      }
      if (formData.introduction !== (user.introduction || '')) {
        updateData.introduction = formData.introduction;
      }

      // 변경사항이 없으면 편집 모드 종료
      if (Object.keys(updateData).length === 0) {
        setIsEditing(false);
        setSuccessMessage('변경사항이 없습니다.');
        return;
      }

      // API 호출
      const updatedAuthor = await authorAPI.editAuthor(user.id, updateData);

      // 사용자 정보 업데이트
      const updatedUser = {
        ...user,
        email: updatedAuthor.email,
        introduction: updatedAuthor.introduction,
        updatedAt: updatedAuthor.updatedAt
      };

      setUser(updatedUser);
      localStorage.setItem('user', JSON.stringify(updatedUser));

      setIsEditing(false);
      setSuccessMessage('작가 정보가 성공적으로 업데이트되었습니다.');

      // 성공 메시지를 3초 후 제거
      setTimeout(() => {
        setSuccessMessage('');
      }, 3000);

    } catch (error) {
      console.error('정보 업데이트 실패:', error);
      
      let errorMessage = '정보 업데이트 중 오류가 발생했습니다.';
      
      if (error.message.includes('이미 등록된 이메일')) {
        setErrors({ email: '이미 등록된 이메일입니다.' });
        errorMessage = '';
      } else if (error.message.includes('기존과 동일한 이메일')) {
        setErrors({ email: '기존과 동일한 이메일입니다.' });
        errorMessage = '';
      }

      if (errorMessage) {
        setErrors({ submit: errorMessage });
      }
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    setFormData({
      email: user.email || '',
      introduction: user.introduction || ''
    });
    setErrors({});
    setIsEditing(false);
  };

  const formatDate = (dateString) => {
    if (!dateString) return '없음';
    try {
      return new Date(dateString).toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return dateString;
    }
  };

  if (isLoading) {
    return (
      <div className={styles.profileContainer}>
        <div style={{ textAlign: 'center', padding: '4rem' }}>
          <div className={styles.loadingSpinner}></div>
          <p style={{ marginTop: '1rem', color: '#6b7280' }}>
            사용자 정보를 불러오는 중...
          </p>
        </div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className={styles.profileContainer}>
        <div style={{ textAlign: 'center', padding: '4rem' }}>
          <span className="material-icons" style={{ fontSize: '4rem', color: '#ef4444' }}>
            error
          </span>
          <h2 style={{ color: '#1f2937', marginTop: '1rem' }}>
            사용자 정보를 찾을 수 없습니다
          </h2>
          <p style={{ color: '#6b7280', marginTop: '0.5rem' }}>
            다시 로그인해 주세요.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.profileContainer}>
      {/* 헤더 */}
      <div className={styles.profileHeader}>
        <h1 className={styles.profileTitle}>
          <span className="material-icons">account_circle</span>
          작가 정보 관리
        </h1>
        <p className={styles.profileSubtitle}>
          계정 정보를 확인하고 수정할 수 있습니다
        </p>
      </div>

      {/* 성공 메시지 */}
      {successMessage && (
        <div className={styles.successMessage}>
          <span className="material-icons">check_circle</span>
          {successMessage}
        </div>
      )}

      <div className={styles.profileContent}>
        {/* 기본 정보 카드 */}
        <div className={styles.profileCard}>
          <div className={styles.cardHeader}>
            <h2 className={styles.cardTitle}>
              <span className="material-icons">person</span>
              기본 정보
            </h2>
            {!isEditing && (
              <button 
                className={styles.editButton}
                onClick={() => setIsEditing(true)}
              >
                <span className="material-icons">edit</span>
                정보 수정
              </button>
            )}
          </div>

          {!isEditing ? (
            <div className={styles.infoGrid}>
              <div className={styles.infoItem}>
                <label className={styles.infoLabel}>이름</label>
                <div className={styles.infoValue}>
                  {user.name || '없음'}
                </div>
              </div>

              <div className={styles.infoItem}>
                <label className={styles.infoLabel}>이메일</label>
                <div className={styles.infoValue}>
                  {user.email || '없음'}
                </div>
              </div>

              <div className={styles.infoItem}>
                <label className={styles.infoLabel}>소개글</label>
                <div className={`${styles.infoValue} ${!user.introduction ? styles.empty : ''}`}>
                  {user.introduction || '소개글이 없습니다'}
                </div>
              </div>
            </div>
          ) : (
            <form className={styles.editForm} onSubmit={(e) => e.preventDefault()}>
              <div className={styles.formGroup}>
                <label className={styles.formLabel}>
                  <span className="material-icons">badge</span>
                  이름
                </label>
                <div className={styles.infoValue}>
                  {user.name} (변경 불가)
                </div>
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
                  disabled={isSaving}
                />
                {errors.email && (
                  <span className={styles.errorMessage}>
                    <span className="material-icons">error</span>
                    {errors.email}
                  </span>
                )}
              </div>

              <div className={styles.formGroup}>
                <label htmlFor="introduction" className={styles.formLabel}>
                  <span className="material-icons">description</span>
                  소개글
                </label>
                <textarea
                  id="introduction"
                  name="introduction"
                  value={formData.introduction}
                  onChange={handleChange}
                  className={`${styles.formInput} ${styles.formTextarea} ${errors.introduction ? styles.error : ''}`}
                  placeholder="자신을 소개하는 글을 작성해주세요 (선택사항)"
                  disabled={isSaving}
                />
                <div style={{ fontSize: '0.875rem', color: '#6b7280', textAlign: 'right' }}>
                  {formData.introduction.length}/500
                </div>
                {errors.introduction && (
                  <span className={styles.errorMessage}>
                    <span className="material-icons">error</span>
                    {errors.introduction}
                  </span>
                )}
              </div>

              {errors.submit && (
                <div className={styles.errorMessage}>
                  <span className="material-icons">error</span>
                  {errors.submit}
                </div>
              )}

              <div className={styles.formActions}>
                <button
                  type="button"
                  className={styles.cancelButton}
                  onClick={handleCancel}
                  disabled={isSaving}
                >
                  <span className="material-icons">close</span>
                  취소
                </button>
                <button
                  type="button"
                  className={styles.saveButton}
                  onClick={handleSave}
                  disabled={isSaving}
                >
                  {isSaving ? (
                    <>
                      <div className={styles.loadingSpinner}></div>
                      저장 중...
                    </>
                  ) : (
                    <>
                      <span className="material-icons">save</span>
                      저장
                    </>
                  )}
                </button>
              </div>
            </form>
          )}
        </div>

        {/* 계정 상태 카드 */}
        <div className={styles.profileCard}>
          <div className={styles.cardHeader}>
            <h2 className={styles.cardTitle}>
              <span className="material-icons">verified_user</span>
              계정 상태
            </h2>
          </div>

          <div className={styles.infoGrid}>
            <div className={styles.infoItem}>
              <label className={styles.infoLabel}>승인 상태</label>
              <div className={styles.infoValue}>
                <span className={`${styles.statusBadge} ${user.isApproved ? styles.approved : styles.pending}`}>
                  <span className="material-icons">
                    {user.isApproved ? 'check_circle' : 'pending'}
                  </span>
                  {user.isApproved ? '승인됨' : '승인 대기 중'}
                </span>
              </div>
            </div>

            <div className={styles.infoItem}>
              <label className={styles.infoLabel}>계정 유형</label>
              <div className={styles.infoValue}>
                {user.isAdmin ? '관리자' : '작가'}
              </div>
            </div>

            <div className={styles.infoItem}>
              <label className={styles.infoLabel}>가입일</label>
              <div className={styles.infoValue}>
                {formatDate(user.createdAt)}
              </div>
            </div>

            <div className={styles.infoItem}>
              <label className={styles.infoLabel}>최종 수정일</label>
              <div className={styles.infoValue}>
                {formatDate(user.updatedAt)}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthorProfile;
