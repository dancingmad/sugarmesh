import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Graph } from './graph';
import {Observable, Subject} from 'rxjs';
import { NotyService } from './noty.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class GraphService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  private graphUrl = '/api/dependencies';
  private pendingGraph = new Subject<Graph>();
  private fetchedGraph: Graph;

  constructor(private http: HttpClient,
              private notyService: NotyService) {
  }

  addGraph(graph:Graph):Observable<Graph> {
    // POST for adding entity to collection, it is NOT idempotent
    return this.http.post<Graph>(`${this.graphUrl}/`, graph).pipe(
      catchError(this.notyService.handleError('Could not add Course')),
    );
  }
}
