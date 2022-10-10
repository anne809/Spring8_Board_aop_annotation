$(function(){
   
   /*
    * 테이블을 숨겨주고, 더보기 기능을 추가합니다. 전체 댓글갯수 , 글의 총 갯수 (BOARDLIST 그대로 가져온것입니다) 더보기를
    * 추가할건데 마지막 페이지에선 숨겨줘야겠죠? 0이 아니면 첫페이지의 댓글을 구해옵니다. 댓글이 없는 경우까지 처리합니다.
    */
   
   $("comment table").hide();// 1
   var page=1;// 더보기에서 보여줄 페이지를 기억할 변수
   var count=0;// 전체 댓글 갯수
   var maxPage=getMaxPage(); // 댓글의 총 페이지 수를 구합니다.
   if(count !=0) // 댓글 갯수가 0이 아니면
      getList(1); // 첫페이지의 댓글을 구해옵니다 ( 한페이지에 3개씩 가져옵니다.
   else{ // 댓글이 없는 경우
      $("#message").text("등록된 댓글이 없습니다.")      
   }
   
   // 최대 몇 페이지를 가질 수 있는지 계산합니다.
   function getMaxPage(){
      // 글의 총 갯수 - 등록 또는 삭제시 댓글의 총 갯수 변화가 있기 때문에 갯수를 구합니다.
      count = parseInt($("#count").text());
      
      // 예) 총 리스트가 5개 이면 최대 페이지는 2입니다.
      // int maxpage = (listcount + limit - 1) / limit;
      return parseInt((count + 3 - 1 ) / 3 ) ;
      
      
   }

   
   // qna_board_view에서 <input type="hidden" name="num" value="${param.num}"
   // id="board_num"> id값 맞춰야합니다.
   // 보여줄 부분 , 숨길 부분 설정하는 곳...
   function getList(currentPage){
      $.ajax({
         type : "post",
         url  : "CommentList.bo",
         data : {
            "board_num" : $("#board_num").val(),
            "page"      : currentPage    //페이지 값들어오면 row start 참조하는거...
         },
         dataType : "json",
         success : function(rdata){
            if (rdata.length > 0 ) {
               $("comment table").show(); // 문서가 로딩될 때 hide() 했던 부분을 보이게
                                    // 합니다(1)
               $("comment thead").show(); // 글이 없을때 hide() 부분을 보이게 합니다(2)
               output = '';
               $(rdata).each(function(){
                           img = '';
                           if ($("#loginid").val() == this.id){ //수정삭제는 글 작성자만 수정, 삭제 할수있도록...
                              // 업데이트할때의 이미지들을 가져다 놓습니다.
                              img = "<img src='resources/image/pencil2.png' width='15px' class='update'>"
                                 + "<img src='resources/image/remove.png' width='15px' class='remove'>"
                                 + "<input type='hidden' value='"
                                 + this.num + "'>"; // 댓글을 달때의 번호
                                                // 반드시 히든으로 갖고
                                                // 있어야합니다.
                           }
                           output +="<tr><td>" + this.id + "</td>";
                           output +="<td>" + this.content + "</td>";
                           output +="<td>" + this.reg_date + img +  "</td></tr>";   // 위에서
                                                                     // 만든
                                                                     // 이미지
                                                                     // 출력
               }); // each end
            
               $("#comment tbody").append(output);
               
               console.log("현재" + currentPage)
               console.log("max" + maxPage)
               // 현재 페이지가 마지막 페이지면 "더보기"는 나타나지 않습니다.
               if(currentPage == maxPage){
                  $("#message").text("");                  
               } else {
                  $("#message").text("더보기")
               } 
               
               // 더보기를 클릭 할 경우 현재 페이지에서 1증가된 페이지를 보여주기 위해 값을 설정합니다.
               page=currentPage + 1;
            } else {
               $("#message").text("등록된 댓글이 없습니다.")
               $("#comment thead").hide(); // 2
               $("#message tbody").empty(); // 데이터가 한 개 인 경우 삭제하면서
                                       // tdody를 비웁니다.
               }
               
               
            }
            
         }); //ajax end
      }//function end
   
//더보기를 클릭하면 page 내용이추가로 보여집니다.
   $("#message").click(function(){
      getList(page);
   });//click end
   
   
   
//글자수 50개 제한하는 이벤트
   $("#content").on('input',function(){
      length = $(this).val().length;
      if ( length > 50){
          length = 50;
          content = content.substring(0, length);
         
      }
      $(".float-left").text(length + "/50") // 뷰단에서 글자수가 뜹니다. 3/50 이런식으로글자수에 따라 실시간 변경
   })
/*
 * 원래 스스로 고민을 해보시면 수월하게 할수있어요. 제가(선생님) 쓴걸 참고로해서 여러분 나름대로 스타일로 바꾸셔도 됩니다~  */   
   
   
   
   
   //등록 또는 수정완료 버튼을 클릭한 경우 버튼 라벨로 add, update할지.. = url내부만 바뀐다는뜻입니다. 버튼이 등록이면 등록하게 되고 else면 댓글을 수정..
   //add와 update는 같은 메서드안에서 처리...
   
   //등록 또는 수정완료 버튼을 클릭한 경우
   //버튼의 라벨이 '등록'인 경우는 댓글을 추가하는 경우
   //버튼의 라벨이 '수정완료' 인 경우는 댓글을 수정하는 경우
   $("#write").click(function(){
      buttonText= $("#write").text(); // 버튼의 라벨로 add할지 update할지 결정
      content = $("#content").val();
      $(".float-left").text('총 50자까지 가능합니다.');
      
      if(buttonText == "등록") { // 댓글을 추가하는 경우
         url = "CommentAdd.bo";
         data = {
               "content" : content,
               "id" : $("#loginid").val(),
               "board_num" : $("#board_num").val()               
         };
      } else {//댓글을 수정하는 경우
         url = "CommentUpdate.bo";
         data = {
               "num" : num,
               "content" : content               
         };
         $("#write").text("등록"); //다시 등록으로 변경, 버튼글자가 변경됩니다.
      }
      
      //위에서 글을 완료했으면 데이터를 보내는 부분입니다. post로 보내고.. add나 업데이트가 끝났으면 수행하는 부분이죠.
      $.ajax({
         type : "post",
         url : url,
         data : data,
         success : function(result){
            $("#content").val('');
            if(result == 1){
               $("#comment tbody").empty();
               if(url == "CommentAdd.bo"){
                  $("#count").text(++count); //등록을 클릭하면 전체 댓글 갯수 증가합니다.
                  maxPage=getMaxPage(); //maxPage 다시 구합니다.                  
               }
               getList(1); //등록 , 수정 완료후 첫페이지 보여줍니다.               
            }
         }         
      }) //ajax end      
   }) // $("#write") end
   
   
   
   
   
   //pencil2.png를 클릭하는 경우(수정) -> 나를 기준으로 부모를 찾아가고 바로 앞에있는 형제의textnode값-> 이걸 content에 뿌려줍니다.
   //중요한건 수정했을때 num이 넘어와야하는게 가장 중요합니다. hidden으로 숨겨놓았던것을 , 내 형제 기준 뒤 뒤 next next 두칸 뒤로 가면 num이 있다.-> 이걸 가져오는거에요.
   //그리고 나를 기점으로 색상변경...
   //이부분을 잘 해주셔야합니다. 
   $("#comment").on('click', '.update', function(){
      before = $(this).parent().prev().text(); //선택한 내용을 가져옵니다. this를 기점으로 
      $("#content").focus().val(before); //textarea에 수정전 내용을 보여줍니다.
       num = $(this).next().next().val();
       $("#write").text("수정완료"); //등록 버튼의 라벨을 '수정완료' 로 변경합니다.
       $(this).parent().parent().css('background', 'lightgray'); //수정할 행의 배경색을 변경합니다.
         
   })
   
   //remove.png를 클릭하는 경우
   $("#comment").on('click', '.remove', function(){ //on매개변수 3개짜리 예제 찾아보기.
      var num = $(this).next().val(); //댓글 번호
      $.ajax({
         type : "post",
         url : "CommentDelete.bo",
         data : {
            "num" : num            
         },
         success : function(result){
            if(result == 1) {
               $("#comment tbody").empty();
               $("#count").text(--count); //삭제하면 전체 댓글 갯수 한개 줄어듭니다.
               maxPage=getMaxPage(); //maxPage 다시 구합니다.
               getList(1);
            }
            
         }
      })//ajax end
   })
   
});//ready
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   