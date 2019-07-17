difference () {
  union () {
    linear_extrude (height=3){
      circle ($fn=100, r=40);
    }
    linear_extrude (height=20){
      difference () {
        resize ([25, 25.9, 1]){
          circle ($fn=100, r=1);
        }
        scale ([0.8, 0.8, 0.8]) {
          resize ([25, 25.9, 1]){
            circle ($fn=100, r=1);
          }
        }
      }
    }
  }
  linear_extrude (height=20, center=true){
    scale ([0.8, 0.8, 0.8]) {
      resize ([25, 25.9, 1]){
        circle ($fn=100, r=1);
      }
    }
  }
}
