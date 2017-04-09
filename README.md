## Synopsis

CLOB Matching Engine implementation. The implementation focues on the matching engine and ignores order management and multiple securities.

## Limitations

The implementation is intended to run in a single thread. It is NOT THREAD SAFE.
Orders, once submitted to the matching engine, can't be cancelled or modified and life forever.
Realisticly each security would have its own order book. This is not shown in the example.
