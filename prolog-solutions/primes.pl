
% divisors by value
prime_divisors(N, Divisors) :-
	nonvar(N), !,
	find_divisors(2, N, Divisors).

find_divisors(Cur, Value, []) :-
	Value < 2, !.

find_divisors(Cur, Value, [Cur, Cur]) :-
	Value is Cur * Cur, !.

find_divisors(Cur, Value, [Value]) :-
	Squared is Cur * Cur,
	Squared > Value, !.

find_divisors(Cur, Value, [Cur | Rec]) :-
	0 is mod(Value, Cur), !,
	Next is Value // Cur,
	find_divisors(Cur, Next, Rec).

find_divisors(Cur, Value, List) :-
# :NOTE: единственный четный делитель это 2, можно уменьшить количество проверок в 2 раза
	Next is Cur + 1,
	find_divisors(Next, Value, List).

% value by divisors
prime_divisors(N, Divisors) :-
	nonvar(Divisors),
	check_ascending(Divisors),
	multiply(1, Divisors, N).

check_ascending([]) :- !.
check_ascending([N]) :- !.

check_ascending([First, Second | Rest]) :-
	First =< Second,
	check_ascending([Second | Rest]).

multiply(A, [], A) :- !.

multiply(A, [First | Rest], R) :-
	multiply(A, Rest, Rec),
	R is Rec * First.


% prime and composite values
init(N) :-
	sieve(2, N).

sieve(A, N) :-
	A > N, !.

sieve(A, N) :-
	A =< N,
	check(A, N),
	AM1 is A + 1,
	sieve(AM1, N).

check(A, N) :-
	composite(A), !.

check(A, N) :-
	AMA is A * A,
	add_mults(AMA, A, N).

add_mults(Cur, A, N) :-
	Cur > N, !.

add_mults(Cur, A, N) :-
	assert(composite(Cur)),
	NEXT is Cur + A,
	add_mults(NEXT, A, N).

prime(N) :-
	not(composite(N)).


% compact: divisors by value
compact_prime_divisors(N, CDs) :-
	nonvar(N), !,
	prime_divisors(N, Divisors),
	squeeze(Divisors, CDs).

squeeze([], []) :- !.
squeeze([N], [(N, 1)]) :- !.

squeeze([First | Rest], [(First, NP1) | AnswRest]) :-
	squeeze(Rest, [(First, N) | AnswRest]), !,
	NP1 is N + 1.

squeeze([First | Rest], [(First, 1) | AnswRest]) :-
	squeeze(Rest, AnswRest).

%compact: value by divisors
compact_prime_divisors(N, CDs) :-
	nonvar(CDs),
	unsqueeze(CDs, Divisors),
	prime_divisors(N, Divisors).

unsqueeze([], []) :- !.
unsqueeze([(A, 1) | Rest], [A | Rec]) :-
	!,
	unsqueeze(Rest, Rec).
unsqueeze([(A, N) | Rest], [A | Rec]) :-
	NM1 is N - 1,
	unsqueeze([(A, NM1) | Rest], Rec).
