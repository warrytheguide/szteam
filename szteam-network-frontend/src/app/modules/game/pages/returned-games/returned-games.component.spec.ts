import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReturnedGamesComponent } from './returned-games.component';

describe('ReturnedGamesComponent', () => {
  let component: ReturnedGamesComponent;
  let fixture: ComponentFixture<ReturnedGamesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReturnedGamesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReturnedGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
