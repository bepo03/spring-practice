# 09-git-branch-strategy

Git 브랜치 전략을 직접 시뮬레이션하며 Git Flow, GitHub Flow, Trunk-Based Development, SemVer, Branch Protection의 차이를 확인하는 실습입니다.

이번 실습은 Spring Boot 애플리케이션 구현이 아니라 Git 작업 흐름을 익히는 것이 목표입니다.

## 수업 주제

Day 06: Git 브랜치 전략

## 실습 목표

- Git Flow의 feature, release, hotfix 흐름을 직접 만들어본다.
- GitHub Flow의 짧은 브랜치와 main 병합 흐름을 확인한다.
- Trunk-Based Development에서 Feature Flag가 필요한 이유를 이해한다.
- SemVer와 Git Tag로 릴리즈 버전을 관리한다.
- GitHub Branch Protection으로 main 브랜치 보호 규칙을 설정한다.

## 실습 목록

| 번호 | 실습 | 주제 |
| --- | --- | --- |
| 실습 1 | Git Flow 시뮬레이션 | develop, feature, release, hotfix, tag |
| 실습 2 | GitHub Flow 시뮬레이션 | main과 짧은 작업 브랜치 |
| 실습 3 | Trunk-Based + Feature Flag | 짧은 브랜치, flag OFF/ON, flag 제거 |
| 실습 4 | SemVer + Tag 실습 | MAJOR, MINOR, PATCH, annotated tag |
| 실습 5 | Branch Protection 설정 | GitHub main 보호 규칙 |

## 진행 방식

- 수업 자료의 명령어는 직접 보고 따라 입력한다.
- 이 README는 진행 기록과 체크리스트 용도로만 사용한다.
- 실습용 임시 Git 저장소는 상위 `spring-practice` Git 저장소 안에 중첩해서 만들지 않는다.
- 로컬에서 명령어를 따라칠 때는 필요하면 `C:\Users\wjsdb\Downloads` 아래 별도 폴더에서 진행한다.
- 각 실습이 끝나면 Git log, tag, branch 상태를 확인하고 이 README 체크리스트를 갱신한다.

## 실습 1. Git Flow 시뮬레이션

### 확인할 흐름

```text
main
  -> develop
      -> feature/user-login
      -> feature/user-signup
      -> release/1.0.0
main
  -> hotfix/1.0.1-login-bug
```

### 체크리스트

- [x] `develop` 브랜치 생성
- [x] `feature/user-login` 브랜치 작업 후 `develop`에 병합
- [x] `feature/user-signup` 브랜치 작업 후 `develop`에 병합
- [x] `release/1.0.0` 브랜치 생성
- [x] `main`에 release 병합
- [x] `v1.0.0` 태그 생성
- [x] `develop`에 release 백포트
- [x] `hotfix/1.0.1-login-bug` 브랜치 생성
- [x] `main`에 hotfix 병합
- [x] `v1.0.1` 태그 생성
- [x] `develop`에 hotfix 백포트
- [x] `git log --all --oneline --graph --decorate`로 흐름 확인

## 실습 2. GitHub Flow 시뮬레이션

### 확인할 흐름

```text
main
  -> feat/search
  -> feat/login
  -> fix/search-bug
```

### 체크리스트

- [x] `feat/search` 브랜치 작업 후 `main`에 병합
- [x] `feat/login` 브랜치 작업 후 `main`에 병합
- [x] `fix/search-bug` 브랜치 작업 후 `main`에 병합
- [x] 작업 브랜치 삭제
- [x] `git log --all --oneline --graph`로 Git Flow와 차이 비교

## 실습 3. Trunk-Based + Feature Flag

### 확인할 흐름

```text
main
  -> improve-search-algo
  -> enable-new-search
  -> cleanup-search-flag
```

### 체크리스트

- [x] Feature Flag 기본값 OFF 구조 생성
- [x] 새 검색 알고리즘을 짧은 브랜치에서 구현
- [x] flag OFF 상태로 `main`에 병합
- [x] 별도 브랜치에서 flag ON 전환
- [x] 안정화 후 flag 제거 브랜치 생성
- [x] `git log --oneline --graph`로 짧은 브랜치 흐름 확인

## 실습 4. SemVer + Tag 실습

### 확인할 버전

| 버전 | 의미 |
| --- | --- |
| `v1.0.0` | 첫 정식 릴리즈 |
| `v1.1.0` | 호환 가능한 기능 추가 |
| `v1.1.1` | 버그 수정 |
| `v2.0.0` | 호환성이 깨지는 변경 |

### 체크리스트

- [ ] `VERSION`을 `1.0.0`으로 변경하고 `v1.0.0` 태그 생성
- [ ] 기능 추가 후 `1.1.0`, `v1.1.0` 생성
- [ ] 버그 수정 후 `1.1.1`, `v1.1.1` 생성
- [ ] Breaking Change 후 `2.0.0`, `v2.0.0` 생성
- [ ] `git tag -l`로 전체 태그 확인
- [ ] `git tag -l "v1.*"`로 v1 태그만 확인
- [ ] `git log v1.0.0..v2.0.0 --oneline`으로 버전 간 변경 확인
- [ ] `git show v2.0.0`으로 태그 메시지 확인

## 실습 5. Branch Protection 설정

### GitHub에서 설정할 항목

```text
Branch name pattern: main

Require a pull request before merging
Require status checks to pass before merging
Require conversation resolution before merging
Do not allow bypassing the above settings
Allow force pushes: off
Allow deletions: off
```

### 체크리스트

- [ ] GitHub 저장소 Settings > Branches로 이동
- [ ] `main` 브랜치 보호 규칙 추가
- [ ] PR 승인 1명 이상 필요하도록 설정
- [ ] status check 통과 필요하도록 설정
- [ ] conversation resolution 필요하도록 설정
- [ ] force push 금지 확인
- [ ] branch deletion 금지 확인
- [ ] 직접 push 시도 시 실패하는지 확인

## 필요한 확인 명령어

```bash
git status
git branch
git branch -a
git log --all --oneline --graph --decorate
git tag -l
```

## 구현 체크리스트

- [x] 실습 1 Git Flow 시뮬레이션 완료
- [x] 실습 2 GitHub Flow 시뮬레이션 완료
- [x] 실습 3 Trunk-Based + Feature Flag 완료
- [ ] 실습 4 SemVer + Tag 완료
- [ ] 실습 5 Branch Protection 설정 완료
