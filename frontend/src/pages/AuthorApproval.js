import React, { useState, useEffect } from 'react';
import { authorAPI } from '../services/api';
import styles from '../styles/AuthorApproval.module.css';

const AuthorApproval = () => {
  const [pendingAuthors, setPendingAuthors] = useState([]);
  const [approvedAuthors, setApprovedAuthors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [processingIds, setProcessingIds] = useState(new Set());
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('pending'); // 'pending' or 'approved'

  // 페이지 로드 시 작가 목록 가져오기
  useEffect(() => {
    loadAuthors();
  }, []);

  const loadAuthors = async () => {
    try {
      setLoading(true);
      setError('');
      
      // 모든 작가 목록 가져오기
      const allAuthors = await authorAPI.getAuthors();
      
      // 배열로 변환 (Spring Data REST 응답 형태 처리)
      const authorsArray = allAuthors._embedded ? allAuthors._embedded.authors : 
                          Array.isArray(allAuthors) ? allAuthors : [];
      
      // 승인 상태별로 분리
      const pending = authorsArray.filter(author => !author.isApproved && !author.isAdmin);
      const approved = authorsArray.filter(author => author.isApproved && !author.isAdmin);
      
      setPendingAuthors(pending);
      setApprovedAuthors(approved);
      
      console.log('승인 대기 작가:', pending.length);
      console.log('승인된 작가:', approved.length);
      
    } catch (err) {
      console.error('작가 목록 로드 실패:', err);
      setError('작가 목록을 불러오는데 실패했습니다: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (author) => {
    try {
      setProcessingIds(prev => new Set([...prev, author.id]));
      
      await authorAPI.approveAuthor(author.id);
      
      // 성공 시 목록 새로고침
      await loadAuthors();
      
      alert(`${author.name} 작가가 승인되었습니다.`);
      
    } catch (err) {
      console.error('작가 승인 실패:', err);
      alert('작가 승인에 실패했습니다: ' + err.message);
    } finally {
      setProcessingIds(prev => {
        const newSet = new Set(prev);
        newSet.delete(author.id);
        return newSet;
      });
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const AuthorRow = ({ author, isApproved }) => (
    <div className={styles.authorRow}>
      <div className={styles.authorBasicInfo}>
        <div className={styles.authorName}>{author.name}</div>
        <span className={`${styles.statusBadge} ${isApproved ? styles.approved : styles.pending}`}>
          {isApproved ? '승인됨' : '승인 대기'}
        </span>
      </div>
      
      <div className={styles.authorEmail}>{author.email}</div>
      
      <div className={styles.authorIntroduction}>
        {author.introduction || '소개글이 없습니다.'}
      </div>
      
      <div className={styles.authorDates}>
        <div><strong>가입일:</strong> {formatDate(author.createdAt)}</div>
        {isApproved && (
          <div><strong>승인일:</strong> {formatDate(author.updatedAt)}</div>
        )}
      </div>
      
      <div className={styles.authorActions}>
        {!isApproved ? (
          <button
            onClick={() => handleApprove(author)}
            disabled={processingIds.has(author.id)}
            className={`${styles.approveBtn} ${processingIds.has(author.id) ? styles.processing : ''}`}
          >
            {processingIds.has(author.id) ? '승인 중...' : '승인'}
          </button>
        ) : (
          <div className={styles.approvedStatus}>승인 완료</div>
        )}
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className={styles.container}>
        <div className={styles.loading}>
          <div className={styles.spinner}></div>
          <p>작가 목록을 불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>작가 승인 관리</h1>
        <p>작가 가입 신청을 검토하고 승인하세요.</p>
      </div>

      {error && (
        <div className={styles.error}>
          <span className="material-icons">error</span>
          {error}
        </div>
      )}

      <div className={styles.tabs}>
        <button
          onClick={() => setActiveTab('pending')}
          className={`${styles.tab} ${activeTab === 'pending' ? styles.active : ''}`}
        >
          승인 대기 ({pendingAuthors.length})
        </button>
        <button
          onClick={() => setActiveTab('approved')}
          className={`${styles.tab} ${activeTab === 'approved' ? styles.active : ''}`}
        >
          승인 완료 ({approvedAuthors.length})
        </button>
      </div>

      <div className={styles.content}>
        {activeTab === 'pending' && (
          <div className={styles.tabContent}>
            {pendingAuthors.length === 0 ? (
              <div className={styles.emptyState}>
                <span className="material-icons">check_circle</span>
                <h3>승인 대기 중인 작가가 없습니다</h3>
                <p>모든 작가 신청이 처리되었습니다.</p>
              </div>
            ) : (
              <div className={styles.authorList}>
                <div className={styles.listHeader}>
                  <div className={styles.headerBasicInfo}>작가 정보</div>
                  <div className={styles.headerEmail}>이메일</div>
                  <div className={styles.headerIntroduction}>소개글</div>
                  <div className={styles.headerDates}>날짜 정보</div>
                  <div className={styles.headerActions}>승인</div>
                </div>
                {pendingAuthors.map(author => (
                  <AuthorRow
                    key={author.id}
                    author={author}
                    isApproved={false}
                  />
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'approved' && (
          <div className={styles.tabContent}>
            {approvedAuthors.length === 0 ? (
              <div className={styles.emptyState}>
                <span className="material-icons">person_add_disabled</span>
                <h3>승인된 작가가 없습니다</h3>
                <p>아직 승인된 작가가 없습니다.</p>
              </div>
            ) : (
              <div className={styles.authorList}>
                <div className={styles.listHeader}>
                  <div className={styles.headerBasicInfo}>작가 정보</div>
                  <div className={styles.headerEmail}>이메일</div>
                  <div className={styles.headerIntroduction}>소개글</div>
                  <div className={styles.headerDates}>날짜 정보</div>
                  <div className={styles.headerActions}>상태</div>
                </div>
                {approvedAuthors.map(author => (
                  <AuthorRow
                    key={author.id}
                    author={author}
                    isApproved={true}
                  />
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default AuthorApproval;
